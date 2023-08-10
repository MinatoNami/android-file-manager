package com.simplemobiletools.filemanager.pro.activities

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import com.simplemobiletools.commons.dialogs.EnterPasswordDialog
import com.simplemobiletools.commons.dialogs.FilePickerDialog
import com.simplemobiletools.commons.extensions.*
import com.simplemobiletools.commons.helpers.NavigationIcon
import com.simplemobiletools.commons.helpers.ensureBackgroundThread
import com.simplemobiletools.commons.helpers.isOreoPlus
import com.simplemobiletools.filemanager.pro.R
import com.simplemobiletools.filemanager.pro.adapters.DecompressItemsAdapter
import com.simplemobiletools.filemanager.pro.extensions.config
import com.simplemobiletools.filemanager.pro.models.ListItem
import kotlinx.android.synthetic.main.activity_decompress.*
import net.lingala.zip4j.exception.ZipException
import net.lingala.zip4j.exception.ZipException.Type
import net.lingala.zip4j.io.inputstream.ZipInputStream
import net.lingala.zip4j.model.LocalFileHeader
import java.io.BufferedInputStream

class DecompressActivity : SimpleActivity() {
    companion object {
        private const val PASSWORD = "password"
    }

    private val allFiles = ArrayList<ListItem>()
    private var currentPath = ""
    private var uri: Uri? = null
    private var password: String? = null
    private var passwordDialog: EnterPasswordDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        isMaterialActivity = true
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_decompress)
        setupOptionsMenu()
        updateMaterialActivityViews(decompress_coordinator, decompress_list, useTransparentNavigation = true, useTopSearchMenu = false)
        setupMaterialScrollListener(decompress_list, decompress_toolbar)

        uri = intent.data
        if (uri == null) {
            toast(R.string.unknown_error_occurred)
            return
        }

        password = savedInstanceState?.getString(PASSWORD, null)

        val realPath = getRealPathFromURI(uri!!)
        decompress_toolbar.title = realPath?.getFilenameFromPath() ?: Uri.decode(uri.toString().getFilenameFromPath())
        setupFilesList()
    }

    override fun onResume() {
        super.onResume()
        setupToolbar(decompress_toolbar, NavigationIcon.Arrow)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(PASSWORD, password)
    }

    private fun setupOptionsMenu() {
        decompress_toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.decompress -> decompressFiles()
                else -> return@setOnMenuItemClickListener false
            }
            return@setOnMenuItemClickListener true
        }
    }

    private fun setupFilesList() {
        fillAllListItems(uri!!)
        updateCurrentPath("")
    }

    override fun onBackPressed() {
        if (currentPath.isEmpty()) {
            super.onBackPressed()
        } else {
            val newPath = if (currentPath.contains("/")) currentPath.getParentPath() else ""
            updateCurrentPath(newPath)
        }
    }

    private fun updateCurrentPath(path: String) {
        currentPath = path
        try {
            val listItems = getFolderItems(currentPath)
            DecompressItemsAdapter(this, listItems, decompress_list) {
                if ((it as ListItem).isDirectory) {
                    updateCurrentPath(it.path)
                }
            }.apply {
                decompress_list.adapter = this
            }
        } catch (e: Exception) {
            showErrorToast(e)
        }
    }

    private fun decompressFiles() {
        val defaultFolder = getRealPathFromURI(uri!!) ?: internalStoragePath
        FilePickerDialog(this, defaultFolder, false, config.showHidden, true, true, showFavoritesButton = true) { destination ->
            handleSAFDialog(destination) {
                if (it) {
                    ensureBackgroundThread {
                        decompressTo(destination)
                    }
                }
            }
        }
    }

    private fun decompressTo(destination: String) {
        try {
            val inputStream = contentResolver.openInputStream(uri!!)
            val zipInputStream = ZipInputStream(BufferedInputStream(inputStream!!))
            if (password != null) {
                zipInputStream.setPassword(password?.toCharArray())
            }
            val buffer = ByteArray(1024)

            zipInputStream.use {
                while (true) {
                    val entry = zipInputStream.nextEntry ?: break
                    val filename = title.toString().substringBeforeLast(".")
                    val parent = "$destination/$filename"
                    val newPath = "$parent/${entry.fileName.trimEnd('/')}"

                    if (!getDoesFilePathExist(parent)) {
                        if (!createDirectorySync(parent)) {
                            continue
                        }
                    }

                    if (entry.isDirectory) {
                        continue
                    }

                    val fos = getFileOutputStreamSync(newPath, newPath.getMimeType())
                    var count: Int
                    while (true) {
                        count = zipInputStream.read(buffer)
                        if (count == -1) {
                            break
                        }

                        fos!!.write(buffer, 0, count)
                    }
                    fos!!.close()
                }

                toast(R.string.decompression_successful)
                finish()
            }
        } catch (e: Exception) {
            showErrorToast(e)
        }
    }

    private fun getFolderItems(parent: String): ArrayList<ListItem> {
        return allFiles.filter {
            val fileParent = if (it.path.contains("/")) {
                it.path.getParentPath()
            } else {
                ""
            }

            fileParent == parent
        }.sortedWith(compareBy({ !it.isDirectory }, { it.mName })).toMutableList() as ArrayList<ListItem>
    }

    @SuppressLint("NewApi")
    private fun fillAllListItems(uri: Uri) {
        val inputStream = try {
            contentResolver.openInputStream(uri)
        } catch (e: Exception) {
            showErrorToast(e)
            return
        }

        val zipInputStream = ZipInputStream(BufferedInputStream(inputStream))
        if (password != null) {
            zipInputStream.setPassword(password?.toCharArray())
        }
        var zipEntry: LocalFileHeader?
        while (true) {
            try {
                zipEntry = zipInputStream.nextEntry
            } catch (passwordException: ZipException) {
                if (passwordException.type == Type.WRONG_PASSWORD) {
                    if (password != null) {
                        toast(getString(R.string.invalid_password))
                        passwordDialog?.clearPassword()
                    } else {
                        askForPassword()
                    }
                    return
                } else {
                    break
                }
            } catch (ignored: Exception) {
                break
            }

            if (zipEntry == null) {
                break
            }

            val lastModified = if (isOreoPlus()) zipEntry.lastModifiedTime else 0
            val filename = zipEntry.fileName.removeSuffix("/")
            val listItem = ListItem(filename, filename.getFilenameFromPath(), zipEntry.isDirectory, 0, 0L, lastModified, false, false)
            allFiles.add(listItem)
        }
        passwordDialog?.dismiss(notify = false)
    }

    private fun askForPassword() {
        passwordDialog = EnterPasswordDialog(
            this,
            callback = { newPassword ->
                password = newPassword
                setupFilesList()
            },
            cancelCallback = {
                finish()
            }
        )
    }
}
