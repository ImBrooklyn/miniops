package main

import "github.com/rivo/tview"

func main() {
    app := tview.NewApplication()
    pages := tview.NewPages()

    form := tview.NewForm().
        AddDropDown("Title", []string{"Mr.", "Ms.", "Mrs.", "Dr.", "Prof."}, 0, nil).
        AddInputField("First name", "", 20, nil, nil).
        AddInputField("Last name", "", 20, nil, nil).
        AddTextArea("Address", "", 40, 0, 0, nil).
        AddTextView("Notes", "This is just a demo.\nYou can enter whatever you wish.", 40, 2, true, false).
        AddCheckbox("Age 18+", false, nil).
        AddPasswordField("Password", "", 10, '*', nil).
        AddButton("Save", func() {
            pages.RemovePage("form")
        }).
        AddButton("Quit", func() {
            app.Stop()
        })
    form.SetBorder(true).SetTitle("Enter some data").SetTitleAlign(tview.AlignLeft)

    pages.AddPage("form", form, true, false)
    pages.ShowPage("form")

    if err := app.SetRoot(pages, true).EnableMouse(true).EnablePaste(true).Run(); err != nil {
        panic(err)
    }
}
